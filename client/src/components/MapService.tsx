"use client";
import { useState, useEffect, useRef, MutableRefObject } from "react";
import {
  GoogleMap,
  useLoadScript,
  Marker,
  Autocomplete,
  Libraries,
} from "@react-google-maps/api";
import WebSocketComponent from "./SocketComponent";

interface LocationPayload {
  clientId: string;
  latitude: number;
  longitude: number;
}

const MapService = ({
  locations,
  sendMessage,
  setCurrentLocation,
  id,
  locationMap,
}: {
  locations: Array<LocationPayload>;
  sendMessage: (message: any) => void;
  setCurrentLocation: (location: any) => void;
  id: string;
  locationMap: Map<string, LocationPayload>;
}) => {
  const [selectedPlace, setSelectedPlace] = useState<any>(null);
  const [searchLngLat, setSearchLngLat] = useState<any>(null);
  const autocompleteRef = useRef<google.maps.places.Autocomplete>();
  const [markers, setMarkers] = useState<Array<google.maps.Marker>>([]);
  const [address, setAddress] = useState("");
  const libraries = ["places"] as Libraries;
  const maps = undefined as unknown as google.maps.Map;
  const libRef = useRef(libraries);
  const [currentMapLocation, setCurrentMapLocation] = useState<any>(null);
  const intervalRef = useRef<NodeJS.Timeout | null>(null);
  const mapRef = useRef(maps);
  const controlAddedRef = useRef(false);
  const [isSyncing, setIsSyncing] = useState(true);
  // Effect to update markers when currentLocation changes

  useEffect(() => {
    const synchronizeTime = () => {
      const now = Date.now();
      const delay = 10000 - (now % 10000); // Time until the next multiple of 5 seconds
      setTimeout(() => {
        handleGetLocationClick(); // Call immediately after the delay
        if (intervalRef.current) clearInterval(intervalRef.current);
        intervalRef.current = setInterval(handleGetLocationClick, 10000); // Continue every 5 seconds
      }, delay);
    };
    if (isLoaded && isSyncing) {
      synchronizeTime();
      return () => {
        if (intervalRef.current) clearInterval(intervalRef.current);
      };
    }
  }, [mapRef.current, isSyncing]);

  useEffect(() => {
    if (isLoaded) {
      setMapOnAll(null);
      setMarkers([]);
      console.log(locationMap);

      const newMarkers: Array<google.maps.Marker> = [];
      Array.from(locationMap).forEach(([clientId, document], i) => {
        let title = clientId;
        let position = { lat: document.latitude, lng: document.longitude };
        const marker = new google.maps.Marker({
          position: position,
          map: mapRef.current,
          title: `${i + 1}. ${title}`,
          label: `${title}`,
          optimized: false,
        });
        newMarkers.push(marker);
      });
      setMarkers(newMarkers);
      var bounds = new google.maps.LatLngBounds();
      for (var i = 0; i < markers.length; i++) {
        bounds.extend(markers[i]!.getPosition()!);
      }

      mapRef.current.fitBounds(bounds);
      if (mapRef!.current!.getZoom()! > 16) {
        mapRef.current.setZoom(16)
      }
    }
  }, [locations]);

  function setMapOnAll(map: google.maps.Map | null) {
    for (let i = 0; i < markers.length; i++) {
      markers[i].setMap(map);
    }
  }

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
    setIsSyncing(false);
    // setCurrentMapLocation(new Map());
  };

  // get current location
  const handleGetLocationClick = () => {
    if (navigator.geolocation) {
      navigator.geolocation.getCurrentPosition(
        (position) => {
          const { latitude, longitude } = position.coords;
          setSelectedPlace(null);
          setSearchLngLat(null);
          sendMessage({
            clientId: id,
            latitude: latitude,
            longitude: longitude,
          });
          // setCurrentMapLocation({ lat: latitude, lng: longitude });
          setIsSyncing(true)
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
    mapRef.current = map;
    mapRef.current.setCenter(center)
    mapRef.current.setZoom(12)
    if (!controlAddedRef.current) {
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
      // mapRef.current = map;
      // const centerControl = new window.google.maps.ControlPosition(
      //   window.google.maps.ControlPosition.TOP_CENTER,
      //   0,
      //   10
      // );

      map.controls[window.google.maps.ControlPosition.TOP_CENTER].push(
        controlDiv
      );
      controlAddedRef.current = true;
    }
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
        <div className="pt-3">
          <input
            type="text"
            id="first_name"
            className="bg-gray-50 border border-gray-300 text-gray-900 text-sm rounded-lg focus:ring-blue-500 focus:border-blue-500 block w-full p-2.5 dark:bg-gray-700 dark:border-gray-600 dark:placeholder-gray-400 dark:text-white dark:focus:ring-blue-500 dark:focus:border-blue-500"
            placeholder="Search for Location"
            required
          />
        </div>
      </Autocomplete>

      {/* map component  */}
      <GoogleMap
        id="map"
        mapContainerClassName="map"
        mapContainerStyle={{ width: "80%", height: "600px", margin: "auto" }}
        onLoad={onMapLoad}
      >
        {selectedPlace && <Marker position={searchLngLat} />}
      </GoogleMap>
    </div>
  );
};

export default MapService;
