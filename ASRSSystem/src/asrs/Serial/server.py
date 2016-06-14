from _thread import start_new_thread

import socket
import serial

#Create socket listener for tcp client
s = socket.socket()
host = ''
port = 12366
s.bind((host, port))
s.listen(1)


def threaded_client(conn):
    try:
        sanya.close()
        erica.close()
    except Exception as ex:
        print('Serial ports are not in use, continueing - %s' % ex)

    def setupSerial(com, baud, type):
        global sanya
        global erica

        if 'SANYA' in type:
            try:
                sanya = serial.Serial(com, baud)
                conn.sendall(b'02|CONNECTED TO SANYA \n')
                start_new_thread(readSanya, ())
            except Exception as e:
                conn.sendall(b'02|ERROR: NOT CONNECTED TO SANYA : \n' + e)
                print(e)
        else:
            try:
                erica = serial.Serial(com, baud)
                conn.sendall(b'02|CONNECTED TO ERICA \n')
                start_new_thread(readErica, ())
            except Exception as e:
                conn.sendall(b'02|ERROR: NOT CONNECTED TO ERICA : \n' + e)
                print(e)

    def serialBridge(input):
        if 'ERICA: EXTENDED' in input:
            writeSanya("PICK UP")
        elif 'SANYA: PICKED UP' in input:
            writeErica("RETRACT")
        elif 'SANYA: EXTEND' in input:
            writeErica("EXTEND")
        else:
            print("NO BRIDGE NEEDED!")

    def readSanya():
        while True:
            retreivedFromSerial = "01|" + sanya.readline().decode() + '\n'
            print(retreivedFromSerial)
            serialBridge(retreivedFromSerial)
            conn.sendall(retreivedFromSerial.encode())

    def readErica():
        while True:
            retreivedFromSerial = "01|" + erica.readline().decode() + '\n'
            print(retreivedFromSerial)
            serialBridge(retreivedFromSerial)
            conn.sendall(retreivedFromSerial.encode())

    def writeSanya(message):
        sanya.write(message.encode())

    def writeErica(message):
        erica.write(message.encode())

    while True:
        data = conn.recv(2048).decode('utf-8')

        try:
            if 'SANYASETUP' in data:
                comvalues = data.split('-')
                com = comvalues[1]
                baud = comvalues[2]
                setupSerial(com, baud, "SANYA")
            elif 'ERICASETUP' in data:
                comvalues = data.split('-')
                com = comvalues[1]
                baud = comvalues[2]
                setupSerial(com, baud, "ERICA")
            elif sanya._isOpen and 'SayToSanya: ' in data:
                data = data[12:]
                writeSanya(data)
                print('WRITTEN TO SANYA: ' + data)
                conn.sendall(b"WRITTEN TO SANYA:\n")
                data = data + "\n"
                conn.sendall(data.encode())
            elif erica._isOpen and 'SayToErica: ' in data:
                data = data[12:]
                writeErica(data)
                print('WRITTEN TO ERICA: ' + data)
                conn.sendall(b"WRITTEN TO ERICA:\n")
                data = data + "\n"
                conn.sendall(data.encode())
        except Exception as ex:
            print('Erica or Sanya is not connected so nothing can be send- %s' % ex)

        if not data:
            break
    conn.close()

#makes connection with client

if __name__ == '__main__':
    """Make a connection with the client"""
    while True:
       global c
       c, addr = s.accept()     # Establish connection with client.
       print('connected to:'+addr[0]+':'+str(addr[1]))
       c.sendall(b'Hello from Python!\n')
       start_new_thread(threaded_client,(c,))





