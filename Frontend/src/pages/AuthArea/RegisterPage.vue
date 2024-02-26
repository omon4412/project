<template>
  <div>
Not implemented
  </div>
</template>

<script>
import checkSession from "@/pages/test";
import router from "@/router/router";

export default {
  async mounted() {
    const session = await checkSession();
    console.log(session + " mainpage " + "mounted")
    if (session !== '') {
      await router.push('/');
    }
  },
}
</script>

<style scoped>

</style>