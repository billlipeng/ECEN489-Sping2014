import serial
import socket

class PythonServer:
    def __init__(self):
        self.ser = serial.Serial('COM5', 115200)
        self.s = socket.socket(socket.AF_INET, socket.SOCK_STREAM)

    def start(self):
        self.host = "localhost"
        self.port = 5556
        self.s.connect((self.host,self.port))
        self.s.send("Lorem Ipsum dolor".encode("UTF-8"))
        self.data = ''
        self.data = self.s.recv(1024).decode();
        print (data)
        self.s.close();

def main():
    ps = PythonServer()
    ps.start()

main()
