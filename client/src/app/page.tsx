import SocketComponent from "../components/SocketComponent";
import { v4 } from "uuid";

export default function Home() {
  interface LocationPayload {
    clientId: string;
    latitude: number;
    longitude: number;
  }
  const id = v4();
  return (
    <div>
      <SocketComponent id={id}/>
    </div>
  );
}
