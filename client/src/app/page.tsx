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
  const locations: Array<LocationPayload> = new Array<LocationPayload>()
  return (
    <div>
      <SocketComponent id={id} locations={locations}/>
    </div>
  );
}
