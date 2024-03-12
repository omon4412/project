<template>
  <div class="app">
    <main-navbar :user="userData"></main-navbar>
    <router-view></router-view>
  </div>
</template>

<script>

import MainNavbar from "@/components/MainNavbar.vue";
import {mapState} from "vuex";

export default {
  data() {
    return {
      userData: ''
    }
  },
  components: {MainNavbar},

  mounted() {
  },
  computed: mapState(['userData']),
  created() {
    this.unsubscribe = this.$store.subscribe((mutation, state) => {
      if (mutation.type === 'updateUser') {
        console.log(`Updating to ${state.userData}`);
        this.userData = state.userData;
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

html {
  position: relative;
  min-height: 100%;
}
</style>