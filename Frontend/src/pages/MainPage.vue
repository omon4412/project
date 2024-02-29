<template>
  <div>
    <h1>hello</h1>
  </div>
</template>

<script>
import router from "@/router/router";

import checkSession from "@/pages/check";

export default {
  async mounted() {
    const session = await checkSession();
    console.log(session + " mainpage " + "mounted")
    if (session === '') {
      await router.push('/login');
    }
  },
}
</script>

<style scoped>

</style>