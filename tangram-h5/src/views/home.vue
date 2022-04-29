<template>
    <a-layout>
        <a-layout-content class="layout-style-with-height">
            <div class="content-style"></div>
        </a-layout-content>
        <my-footer />
    </a-layout>
</template>

<script>
import Footer from '@/components/Footer'
import 'echarts/theme/macarons'
export default {
    components: {
        'my-footer': Footer,
    },
    name: 'home',
    methods: {
        setVisitPageChart() {
            let option = {
                legend: {
                    data: ['name', 'count'],
                },
                tooltip: {
                    trigger: 'axis',
                    axisPointer: {
                        // 坐标轴指示器，坐标轴触发有效
                        type: 'shadow', // 默认为直线，可选为：'line' | 'shadow'
                    },
                },
                grid: {
                    top: 0,
                    left: 0,
                    right: '15px',
                    bottom: 0,
                    containLabel: true,
                },
                yAxis: {
                    type: 'category',
                    data: [
                        ...this.userInfo.urlInfo.map(it => {
                            return it.name
                        }),
                    ],
                },
                xAxis: {
                    type: 'value',
                    boundaryGap: [0, 0.01],
                },
                series: [
                    {
                        data: [
                            ...this.userInfo.urlInfo.map(item => ({
                                name: item.name,
                                value: item.count,
                            })),
                        ],
                        type: 'bar',
                    },
                ],
            }

            let myChart = this.$echarts.init(this.$refs.visitPageChart, 'macarons')
            myChart.setOption(option, true)
        },
        setVisitIpChart() {
            let option = {
                tooltip: {
                    trigger: 'item',
                    formatter: '城市 <br/>{b} : {c} ({d}%)',
                },
                grid: {
                    top: 0,
                    left: 0,
                    right: '15px',
                    bottom: 0,
                    containLabel: true,
                },
                series: [
                    {
                        data: [
                            ...this.userInfo.ipInfo.map(item => ({
                                name: item.ipCity,
                                value: item.count,
                            })),
                        ],
                        type: 'pie',
                        name: 'ipCity',
                    },
                ],
            }

            let myChart = this.$echarts.init(this.$refs.visitIpChart, 'macarons')
            myChart.setOption(option, true)
        },
    },
}
</script>
