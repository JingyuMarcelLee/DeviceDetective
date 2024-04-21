import { useEffect, useId } from "react";
import SocketComponent from "../components/SocketComponent";
import { uuid } from "uuidv4";

export default function Home() {
  const id = uuid();
  const locations:Array<any> = []
  return (
    <div>
      <SocketComponent props={{id, locations}}/>
    </div>
  );
}
