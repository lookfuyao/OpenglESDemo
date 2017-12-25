attribute vec4 aPosition;
uniform mat4 uMatrix;

attribute vec2 aTexCoord;
varying vec2 vTexCoord;

void main() {
  gl_Position = uMatrix * aPosition;
  vTexCoord = aTexCoord;

//  vTexCoord.x = 1. - aTexCoord.x;
//  vTexCoord.y = aTexCoord.y;
}