<template>
  <div class="dashboard-container">
    <component :is="currentRole" />
  </div>
</template>

<script>
  import adminDashboard from './admin'
  import circleChart from "@/views/dashboard/echarts/circleChart";
  import VCharts from 'v-charts'
  export default {
    name: 'Dashboard',
    components: { adminDashboard,},
    data() {
      return {
        currentRole: 'adminDashboard'
      }
    },
  }
</script>
