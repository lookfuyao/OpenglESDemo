precision mediump float;

varying vec2 vTexCoord;
uniform sampler2D uTexture;

void main() {

//    if (vTexCoord.x > 0.25 && vTexCoord.x < 0.75 && vTexCoord.y > 0.25 && vTexCoord.y < 0.75){
//        gl_FragColor = vec4(0,0,0,0);
//    } else {
        gl_FragColor = texture2D(uTexture, vTexCoord);
//    }
}