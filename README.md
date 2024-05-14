[![Review Assignment Due Date](https://classroom.github.com/assets/deadline-readme-button-24ddc0f5d75046c5622901739e7c5dd533143b0c8e959d652212380cedb1ea36.svg)](https://classroom.github.com/a/XwNCTfYP)

# Docker commands (not finalized)
- socat TCP-LISTEN:6001,reuseaddr,fork UNIX-CLIENT:/tmp/.X11-unix/X1
- docker build -t projlab .
- docker run -v /tmp/.X11-unix:/tmp/.X11-unix -e DISPLAY=&lt;host ip addr here&gt;:1 projlab