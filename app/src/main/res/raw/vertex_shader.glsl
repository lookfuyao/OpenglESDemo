attribute vec4 aPosition;
uniform mat4 uMatrix;

uniform vec4 uColor;
varying  vec4 vColor;

void main() {
  gl_Position = uMatrix * aPosition;
  vColor=uColor;
}