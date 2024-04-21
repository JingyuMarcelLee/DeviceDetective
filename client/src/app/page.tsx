import { useEffect, useId } from "react";
import SocketComponent from "../components/SocketComponent";
import { uuid } from "uuidv4";

export default function Home() {
  interface LocationPayload {
    clientId: string;
    latitude: number;
    longitude: number;
  }
  const id = uuid();
  const locations: Map<string, any> = Object()
  return (
    <div>
      <SocketComponent props={{id, locations}}/>
    </div>
  );
}
