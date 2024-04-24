import { useEffect, useId } from "react";
import SocketComponent from "../components/SocketComponent";
import { v4 } from "uuid";

export default function Home() {
  interface LocationPayload {
    clientId: string;
    latitude: number;
    longitude: number;
  }
  const id = v4();
  // const locations: Array<LocationPayload> = new Array<LocationPayload>()
  return (
    <div>
      <SocketComponent id={id}/>
    </div>
  );
}
