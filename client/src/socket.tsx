"use client"

import { io } from "socket.io-client";

const URL = 'http://172.18.57.150/21:8080';

export const socket = io(URL);