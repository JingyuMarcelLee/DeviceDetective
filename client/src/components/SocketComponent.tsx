"use client"

import { useEffect } from 'react';
import SockJS from 'sockjs-client';
import { Client } from '@stomp/stompjs';

const WebSocketComponent: React.FC = () => {
  useEffect(() => {
    // Create a new SockJS connection
    const socket = new SockJS('http://localhost:8080/ws');
    // Create a STOMP client over the SockJS connection
    const client = new Client({
      webSocketFactory: () => socket,  // Use the SockJS connection for the STOMP client
      debug: (str) => {
        console.log(str);
      },
    });

    // Connect to the STOMP server
    client.onConnect = (frame) => {
      console.log('Connected: ' + frame);

      // Subscribe to a topic provided by the server
      client.subscribe('/topic/locations', (message) => {
        // Called when the client receives a message from the subscribed topic
        console.log('Message received: ' + message.body);
      });
    };

    client.onStompError = (frame) => {
      console.error('STOMP Error:', frame.headers.message);
      console.error('STOMP Error Details:', frame.body);
    };

    // Activate the STOMP client
    client.activate();

    return () => {
      client.deactivate();  // Clean up the connection when the component unmounts
    };
  }, []);

  return (
    <div>
      WebSocket Component: React component that establishes WebSocket connection using SockJS and STOMP.
    </div>
  );
};

export default WebSocketComponent;