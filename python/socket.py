# SOCKET, CLIENT EXAMPLE

import socket

mysock = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
mysock.connect(('data.pr4e.org', 80))

cmd = "GET http://data/pr4e.org/page1.html HTTP/1.0\r\n\r\n".encode()
mysock.send(cmd)

while True:
    data = mysock.recv(512)
    if len(data) < 1:
        break
    print(data.decode(), end='')

mysock.close()


# HTTP/1.1 404 Not Found

# Date: Fri, 23 Dec 2022 20:38:36 GMT

# Server: Apache/2.4.18 (Ubuntu)

# Content-Length: 287

# Connection: close

# Content-Type: text/html; charset=iso-8859-1



# <!DOCTYPE HTML PUBLIC "-//IETF//DTD HTML 2.0//EN">
# <html><head>
# <title>404 Not Found</title>
# </head><body>
# <h1>Not Found</h1>
# <p>The requested URL /pr4e.org/page1.html was not found on this server.</p>
# <hr>
# <address>Apache/2.4.18 (Ubuntu) Server at data Port 80</address>
# </body></html>



# LOCAL VERSION
import socket

mysock = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
mysock.connect(('127.0.0.1', 9000))

cmd = "GET http://127.0.0.1/page1.html HTTP/1.0\r\n\r\n".encode()
mysock.send(cmd)

while True:
    data = mysock.recv(512)
    if len(data) < 1:
        break
    print(data.decode(), end='')

mysock.close()



# CLIENT, WITH URLLIB

import urllib.request

fhand = urllib.request.urlopen("http://127.0.0.1:9000/page1.html")
for line in fhand:
    print(line.decode().strip())





# SECKET, SERVER EXAMPLE 

from socket import *


def createSocket():
    serversocket = socket(AF_INET, SOCK_STREAM)
    try:
        serversocket.bind(('localhost', 9000)) # register to listen port 9000
        serversocket.listen(5) # operation system should queue 5 (no more) another requests if the server is busy

        while (1):
            (clientsocket, address) = serversocket.accept() # waiting for incoming request
            # at this point we established the connection, but haven't receive data yet

            rd = clientsocket.recv(5000).decode() # receiving data and decode
            pieces = rd.split("\n")
            if (len(pieces) > 0):
                print(pieces[0])

            data = "HTTP/1.1 200 OK\r\n"
            data += "Content-Type: text/html; charset=utf-8\r\n"
            data += "\r\n"
            data += "<html><body>Hello World</body></html>"
            clientsocket.sendall(data.encode())
            clientsocket.shutdown(SHUT_WR)

    except KeyboardInterrupt:
        print("\nSutting down...\n")
    except Exception as err:
        print("Error")
        print(err)

    serversocket.close()


print("Access http://localhost:9000")
createSocket()
