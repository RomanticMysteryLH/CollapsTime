<template>
  <div id="mywordcloud" :style="{width: '100%', height: '300px'}" :data="worddata"></div>
</template>
<script>
  import * as echarts from "echarts";
  import "echarts-wordcloud/dist/echarts-wordcloud";
  import "echarts-wordcloud/dist/echarts-wordcloud.min";
  import {getCount, queryStasticOfHome} from "@/api/home"
  export default {
    name: "VueWordCloud",
    data () {
      return {
        list:null,
        msg: 'Welcome to Your Vue.js App',
        worddata:[],
      }
    },
    mounted(){
      queryStasticOfHome().then(result=>{
        this.list = result.playSongCount
        for(let key in this.list){
          this.worddata.push(
            {
              name:key,
              value:this.list[key]
            })
        }
        this.initChart();
      })
    },
    methods: {
      initChart() {

        this.chart = echarts.init(document.getElementById("mywordcloud"));
        console.log('lll');
        const option = {
          title: {
            text: "",
            x: "center"
          },
          backgroundColor: "#fff",
          // tooltip: {
          //   pointFormat: "{series.name}: <b>{point.percentage:.1f}%</b>"
          // },
          series: [
            {
              type: "wordCloud",
              //用来调整词之间的距离
              gridSize: 10,
              //用来调整字的大小范围
              // Text size range which the value in data will be mapped to.
              // Default to have minimum 12px and maximum 60px size.
              sizeRange: [14, 60],
              // Text rotation range and step in degree. Text will be rotated randomly in range [-90,                                                                             90] by rotationStep 45
              //用来调整词的旋转方向，，[0,0]--代表着没有角度，也就是词为水平方向，需要设置角度参考注释内容
              // rotationRange: [-45, 0, 45, 90],
              // rotationRange: [ 0,90],
              rotationRange: [0, 0],
              //随机生成字体颜色
              // maskImage: maskImage,
              textStyle: {
                normal: {
                  color: function() {
                    return (
                      "rgb(" +
                      Math.round(Math.random() * 255) +
                      ", " +
                      Math.round(Math.random() * 255) +
                      ", " +
                      Math.round(Math.random() * 255) +
                      ")"
                    );
                  }
                }
              },
              //位置相关设置
              // Folllowing left/top/width/height/right/bottom are used for positioning the word cloud
              // Default to be put in the center and has 75% x 80% size.
              left: "center",
              top: "center",
              right: null,
              bottom: null,
              width: "200%",
              height: "200%",
              //数据
              data: this.worddata
            }
          ]
        };
        this.chart.setOption(option);
      },
    }
  }
</script>
