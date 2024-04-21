import { useEffect, useId } from "react";
import MapService from "../components/MapService";
import SocketComponent from "../components/SocketComponent";

export default function Home() {
  const id = useId();
  const locations:Array<any> = []
  return (
    <div>
      <SocketComponent props={{id, locations}}/>
    </div>
  );
}
