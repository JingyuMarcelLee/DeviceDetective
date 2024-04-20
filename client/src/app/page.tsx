import { useEffect } from "react";
import MapService from "../components/MapService";
import SocketComponent from "../components/SocketComponent";

export default function Home() {
  return (
    <div>
      <MapService />
      <SocketComponent />
    </div>
);
}
