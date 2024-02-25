<template>
  <div>
    <h1>hello</h1>
  </div>
</template>

<script>
// import axios from "axios";
import router from "@/router/router";

import checkSession from "@/pages/test";

export default {
  // methods: {
  //   async checkSession() {
  //     await axios.get('http://localhost:5100/api/v1/user', {withCredentials: true})
  //         .then(response => {
  //           this.username = response.data.username;
  //
  //           this.$store.commit('updateUsername', this.username);
  //           //console.log(this.$store.getters.getUsername);
  //           //console.log(this.username);
  //           router.push('/');
  //         })
  //         .catch((error) => {
  //           console.log(error);
  //           console.log("errorororor");
  //           this.$store.commit('updateUsername', null);
  //           console.log(this.$store.getters.getUsername + 'mainpage');
  //           router.push('/login');
  //         });
  //   }
  // },
  async mounted() {
    const session = await checkSession();
    console.log(session + " mainpage " + "mounted")
    if (session === '') {
      await router.push('/login');
    }
  },
  // async beforeRouteUpdate(to, from, next) {
  //   console.log(to, from, next);
  //   const session = await checkSession();
  //   console.log(session + " mainpage " + "beforeroute")
  //   //console.log(session)
  //   if (session === '') {
  //     next("/login");
  //   } else {
  //     next();
  //   }
  // },
  // async beforeRouteEnter(to, from, next){
  //   console.log(to, from, next);
  //   const session = await checkSession();
  //   console.log(session + " mainpage " + "beforeroute")
  //   if (session === '') {
  //     next("/login");
  //   } else {
  //     next();
  //   }
  // }
}
</script>

<style scoped>

</style>