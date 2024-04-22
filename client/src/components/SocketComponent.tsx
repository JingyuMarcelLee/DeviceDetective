"use client"

import { useEffect, useState } from 'react';
import SockJS from 'sockjs-client';
import { Client } from '@stomp/stompjs';
import MapService from './MapService';


interface LocationPayload {
  clientId: string;
  latitude: number;
  longitude: number;
}

const WebSocketComponent = ({id, locations}: {
  id: string;
  locations: Array<LocationPayload>;
}) => {
  const [client, setClient] = useState<Client | null>(null);
  const [isConnected, setIsConnected] = useState(false);
  const [currentLocations, setCurrentLocations] = useState(new Array())

  const sendMessage = (message: any) => {
    // Check if the client is connected
    console.log('Is Connected:', isConnected); // Check the value of isConnected
    console.log('Client:', client); // Check the value of client
    if (isConnected && client) {
      // Send message to the server
      client!.publish({ destination: '/app/syncLocations', body: JSON.stringify(message) });
      console.log('Message sent:', message);
    } else {
      console.error('WebSocket is not connected.');
    }
  };
  
  useEffect(() => {
    // Create a new SockJS connection
    const socket = new SockJS('http://localhost:8080/ws');
    // Create a STOMP client over the SockJS connection
    const stompClient = new Client({
      webSocketFactory: () => socket,  // Use the SockJS connection for the STOMP client
      debug: (str) => console.log(str),
    });

    setClient(stompClient);
    // Connect to the STOMP server
    setIsConnected(true);
    stompClient.onConnect = (frame) => {
      console.log('Connected: ' + frame);
      // Subscribe to a topic provided by the server
      console.log(isConnected)
      console.log(client)
      stompClient.subscribe('/topic/locations', (message) => {
        // Called when the client receives a message from the subscribed topic
        let parsedMessage: { _id: { timestamp: number; date: string }; clientId: string; latitude: string; longitude: string } = JSON.parse(message.body);
        let locationJSON: LocationPayload = {
          clientId: parsedMessage.clientId,
          latitude: parseFloat(parsedMessage.latitude),
          longitude: parseFloat(parsedMessage.longitude)
        };
      console.log('Message received: ' + message.body);
      // const newLocations = new Map(locations);
      setCurrentLocations(currentLocations => [...currentLocations, locationJSON]);

      });

      stompClient.publish({ destination: '/app/registerClient' })
    };


    stompClient.onStompError = (frame) => {
      console.error('STOMP Error:', frame.headers.message);
      console.error('STOMP Error Details:', frame.body);
    };

    // Activate the STOMP client
    stompClient.activate();

    return () => {
      stompClient.deactivate();  // Clean up the connection when the component unmounts
    };
  }, []);



  return (
    <div>
      <MapService locations={currentLocations} sendMessage={sendMessage} setCurrentLocation={setCurrentLocations} id={id}/>
    </div>
  );
};

export default WebSocketComponent;