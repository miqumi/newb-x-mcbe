#ifdef OPAQUE
$input v_fogColor, v_worldPos, v_underwaterRainTime
#endif

#include <bgfx_shader.sh>
#include <newb/main.sh>

void main() {
#ifdef OPAQUE
  vec3 viewDir = normalize(v_worldPos);
  bool underWater = v_underwaterRainTime.x > 0.5;

  vec3 zenithCol;
  vec3 horizonCol;
  vec3 horizonEdgeCol;
  if (underWater) {
    vec3 fogcol = getUnderwaterCol(v_fogColor);
    zenithCol = fogcol;
    horizonCol = fogcol;
    horizonEdgeCol = fogcol;
  } else {
    float rainFactor = v_underwaterRainTime.y;
    zenithCol = getZenithCol(rainFactor, v_fogColor);
    horizonCol = getHorizonCol(rainFactor, v_fogColor);
    horizonEdgeCol = getHorizonEdgeCol(horizonCol, rainFactor, v_fogColor);
  }

  vec3 skyColor = nlRenderSky(horizonEdgeCol, horizonCol, zenithCol, -viewDir, v_fogColor, v_underwaterRainTime.z, false, underWater, false);
  skyColor = colorCorrection(skyColor);

  gl_FragColor = vec4(skyColor, 1.0);
#else
  gl_FragColor = vec4(0.0,0.0,0.0,0.0);
#endif
}
