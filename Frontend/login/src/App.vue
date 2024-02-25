<template>
  <div class="app">
    <main-navbar :username="userName"></main-navbar>
    <router-view></router-view>
  </div>
</template>

<script>

import MainNavbar from "@/components/MainNavbar.vue";
import {mapState} from "vuex";
// import checkSession from "@/pages/test";

export default {
  data() {
    return {
      userName: ''
    }
  },
  components: {MainNavbar},
  // computed: {
  //   username(){
  //     return this.$store.state.username;
  //   },

  //   '$route': {
  //     handler() {
  //       this.checkAndNavigate();
  //     },
  //     immediate: true, // Вызывать обработчик сразу после создания компонента
  //   },
  // },
  //
  // methods: {
  //   async checkAndNavigate() {
  //     const sessionResult = await checkSession();
  //     if (sessionResult !== undefined) {
  //       console.log("/");
  //       router.push('/'); // Перейти на главную страницу
  //     } else {
  //       console.log("/login");
  //       router.push('/login'); // Перейти на страницу входа
  //     }
  //   },
  // },
  //

  mounted() {
    //checkSession();
  },
  computed: mapState(['username']),
  created() {
    this.unsubscribe = this.$store.subscribe((mutation, state) => {
      if (mutation.type === 'updateUsername') {
        console.log(`Updating to ${state.username}`);
        this.userName = state.username;
        // Do whatever makes sense now
        // if (state.status === 'success') {
        //   this.complex = {
        //     deep: 'some deep object',
        //   };
        // }
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