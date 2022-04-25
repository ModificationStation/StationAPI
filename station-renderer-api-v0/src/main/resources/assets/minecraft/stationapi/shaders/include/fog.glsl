#version 150

vec4 exp_fog(vec4 inColor, float vertexDistance, float density, vec4 fogColor) {
    float fogValue = exp(-density*vertexDistance);
    return vec4(mix(fogColor.rgb, inColor.rgb, fogValue * fogColor.a), inColor.a);
}

vec4 exp2_fog(vec4 inColor, float vertexDistance, float density, vec4 fogColor) {
    float fogValue = exp(-density*pow(vertexDistance, 2.0));
    return vec4(mix(fogColor.rgb, inColor.rgb, fogValue * fogColor.a), inColor.a);
}

vec4 linear_fog(vec4 inColor, float vertexDistance, float fogStart, float fogEnd, vec4 fogColor) {
    if (vertexDistance <= fogStart) {
        return inColor;
    }

    float fogValue = vertexDistance < fogEnd ? smoothstep(fogStart, fogEnd, vertexDistance) : 1.0;
    return vec4(mix(inColor.rgb, fogColor.rgb, fogValue * fogColor.a), inColor.a);
}

vec4 fog(int mode, vec4 inColor, float vertexDistance, float density, float fogStart, float fogEnd, vec4 fogColor) {
    switch (mode) {
        case 0:
        return exp_fog(inColor, vertexDistance, density, fogColor);
        case 1:
        return exp2_fog(inColor, vertexDistance, density, fogColor);
        case 2:
        return linear_fog(inColor, vertexDistance, fogStart, fogEnd, fogColor);
        default:
        return vec4(0, 0, 0, 0);
    }
}

float linear_fog_fade(float vertexDistance, float fogStart, float fogEnd) {
    if (vertexDistance <= fogStart) {
        return 1.0;
    } else if (vertexDistance >= fogEnd) {
        return 0.0;
    }

    return smoothstep(fogEnd, fogStart, vertexDistance);
}

float fog_distance(mat4 modelViewMat, vec3 pos, int shape) {
    if (shape == 0) {
        return length((modelViewMat * vec4(pos, 1.0)).xyz);
    } else {
        float distXZ = length((modelViewMat * vec4(pos.x, 0.0, pos.z, 1.0)).xyz);
        float distY = length((modelViewMat * vec4(0.0, pos.y, 0.0, 1.0)).xyz);
        return max(distXZ, distY);
    }
}
