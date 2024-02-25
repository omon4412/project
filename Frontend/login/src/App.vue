<template>
  <div class="app">
    <main-navbar :username="userName"></main-navbar>
    <router-view></router-view>
  </div>
</template>

<script>

import MainNavbar from "@/components/MainNavbar.vue";
import {mapState} from "vuex";

export default {
  data() {
    return {
      userName: ''
    }
  },
  components: {MainNavbar},

  mounted() {
  },
  computed: mapState(['username']),
  created() {
    this.unsubscribe = this.$store.subscribe((mutation, state) => {
      if (mutation.type === 'updateUsername') {
        console.log(`Updating to ${state.username}`);
        this.userName = state.username;
      }
    });
  },
  beforeUnmount() {
    this.unsubscribe();
  },
}
</script>

<style>
* {
  margin: 0;
  padding: 0;
}
</style>