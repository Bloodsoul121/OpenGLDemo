//顶点变量，存储4个变量的数组
attribute vec4 vPosition;

//接收纹理坐标，接收采样器采样图片的坐标  camera
attribute vec4 vCoord;

// oepngl camera
uniform mat4 vMatrix;

//传给片元着色器 像素点
varying vec2 aCoord;

void main(){
    // gpu  需要渲染的 什么图像   形状
    gl_Position = vPosition;
    // 遍历的 for循环 性能比较低，所以这里是并发的
    aCoord = (vMatrix * vCoord).xy;
}