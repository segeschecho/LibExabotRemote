//Copyright (C) 2013 Sergio E. Gonzalez and Emiliano D. González
//Facultad de Ciencias Exactas y Naturales, Universidad de Buenos Aires, Buenos Aires, Argentina.
// 
//C/C++, Java and XML/YML code for a remote control Exabot Robot
//
//This file is part of LibExabotRemote.
//
//LibExabotRemote is free software: you can redistribute it and/or modify
//it under the terms of the GNU General Public License as published by
//the Free Software Foundation, either version 3 of the License, or
//(at your option) any later version.
//
//LibExabotRemote is distributed in the hope that it will be useful,
//but WITHOUT ANY WARRANTY; without even the implied warranty of
//MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
//GNU General Public License for more details.
//
//You should have received a copy of the GNU General Public License
//along with LibExabotRemote.  If not, see <http:www.gnu.org/licenses/>.
//
//
//Authors:    Sergio E. Gonzalez - segonzalez@dc.uba.ar
//            Emiliano D. González - edgonzalez@dc.uba.ar
//
//Departamento de Computación
//Facultad de Ciencias Exactas y Naturales
//Universidad de Buenos, Buenos Aires, Argentina
//Date: June 2013

package com.exabot.remote;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;


public class ExabotRemote {
	
	
	private static int PORT = 7654;
	
	
	private DatagramSocket socket = null;
	private InetAddress IPAddress = null;
	
	// hostname or robot IP address
	public boolean exaRemoteInitialize(String hostname)
	{
		try {
			// Open socket and initialize host ip
			IPAddress = InetAddress.getByName(hostname);
			socket = new DatagramSocket();
			return true;

		} catch (SocketException e) {
			e.printStackTrace();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		
		return false;
	}

	public void exaRemoteDeinitialize()
	{
		// Close socket and reset ip
		socket.close();
		socket = null;
		IPAddress = null;
	}
	
	// Left and Right must be inside closed interval [-1, 1]
	public void exaRemoteSetMotors(float left, float right) 
	{
		// left, right must be inside closed interval [-1,1]  
		if (left > 1) left = 1; else if (left < -1) left = -1;
		if (right > 1) right = 1; else if (right < -1) right = -1;
		
		// prepare data to send  
		byte[] cmd = new byte[3];
		cmd[0] = 0x3;
		cmd[1] = (byte)Math.round(right * 30);
		cmd[2] = (byte)Math.round(left * 30);
		
		// send data  
		try {
			DatagramPacket sendPacket = new DatagramPacket(cmd, cmd.length, IPAddress, PORT);
			socket.send(sendPacket);
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
