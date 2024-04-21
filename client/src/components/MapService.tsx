"use client";
import { useState, useRef, MutableRefObject } from "react";
import {
  GoogleMap,
  useLoadScript,
  Marker,
  Autocomplete,
  Libraries,
} from "@react-google-maps/api";

import WebSocketComponent from "./SocketComponent";

const MapService = ({
  locations,
  sendMessage,
  id,
}: {
  locations: Array<any>;
  sendMessage: (message: any) => void;
  id: string;
}) => {
  const [selectedPlace, setSelectedPlace] = useState<any>(null);
  const [searchLngLat, setSearchLngLat] = useState<any>(null);
  const [currentLocation, setCurrentLocation] = useState<Array<any>>([null]);
  const autocompleteRef = useRef<google.maps.places.Autocomplete>();
  const [address, setAddress] = useState("");
  const libraries = ["places"] as Libraries;
  const libRef = useRef(libraries);

  // load script for google map
  const googleMapsApiKey: string =
    process.env!.NEXT_PUBLIC_GOOGLE_MAPS_API_KEY!;
  const { isLoaded } = useLoadScript({
    googleMapsApiKey: googleMapsApiKey,
    libraries: libRef.current,
  });

  if (!isLoaded) return <div>Loading....</div>;

  // static lat and lng
  const center = { lat: 40.2027, lng: -77.2008 };

  // handle place change on search
  const handlePlaceChanged = () => {
    if (autocompleteRef.current!) {
      const place: google.maps.places.PlaceResult =
        autocompleteRef.current.getPlace();
      setSelectedPlace(place);
      if (place.geometry!.location) {
        setSearchLngLat({
          lat: place.geometry!.location.lat(),
          lng: place.geometry!.location.lng(),
        });
      }
    }
    setCurrentLocation([null]);
  };

  // get current location
  const handleGetLocationClick = () => {
    if (navigator.geolocation) {
      navigator.geolocation.getCurrentPosition(
        (position) => {
          const { latitude, longitude } = position.coords;
          setSelectedPlace(null);
          setSearchLngLat(null);
          setCurrentLocation((locationArr) => [
            ...locationArr,
            { lat: latitude, lng: longitude },
          ]);
          sendMessage({
            clientId: id,
            latitude: latitude,
            longitude: longitude,
          });
        },
        (error) => {
          console.log(error);
        }
      );
    } else {
      console.log("Geolocation is not supported by this browser.");
    }
  };

  // on map load
  const onMapLoad = (map: google.maps.Map) => {
    const controlDiv = document.createElement("div");
    const controlUI = document.createElement("div");
    controlUI.innerHTML = "Get Location";
    controlUI.style.backgroundColor = "white";
    controlUI.style.color = "black";
    controlUI.style.border = "2px solid #ccc";
    controlUI.style.borderRadius = "3px";
    controlUI.style.boxShadow = "0 2px 6px rgba(0,0,0,.3)";
    controlUI.style.cursor = "pointer";
    controlUI.style.marginBottom = "22px";
    controlUI.style.textAlign = "center";
    controlUI.style.width = "100%";
    controlUI.style.padding = "8px 0";
    controlUI.addEventListener("click", handleGetLocationClick);
    controlDiv.appendChild(controlUI);

    // const centerControl = new window.google.maps.ControlPosition(
    //   window.google.maps.ControlPosition.TOP_CENTER,
    //   0,
    //   10
    // );

    map.controls[window.google.maps.ControlPosition.TOP_CENTER].push(
      controlDiv
    );
  };

  return (
    <div
      style={{
        display: "flex",
        flexDirection: "column",
        justifyContent: "center",
        alignItems: "center",
        gap: "20px",
      }}
    >
      {/* search component  */}
      <Autocomplete
        onLoad={(autocomplete) => {
          console.log("Autocomplete loaded:", autocomplete);
          autocompleteRef.current = autocomplete;
        }}
        onPlaceChanged={handlePlaceChanged}
        options={{ fields: ["address_components", "geometry", "name"] }}
      >
        <input type="text" placeholder="Search for a location" />
      </Autocomplete>

      {/* map component  */}
      <GoogleMap
        zoom={currentLocation[0] || selectedPlace ? 18 : 12}
        center={currentLocation[0] || searchLngLat || center} // CHANGE THIS TO ID BASED
        mapContainerClassName="map"
        mapContainerStyle={{ width: "80%", height: "600px", margin: "auto" }}
        onLoad={onMapLoad}
      >
        {selectedPlace && <Marker position={searchLngLat} />}
        {currentLocation &&
          currentLocation.map((location, index) => (
            <Marker key={index} position={location} />
          ))}
      </GoogleMap>
    </div>
  );
};

export default MapService;
