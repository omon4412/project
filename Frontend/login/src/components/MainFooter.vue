<template>
  <div class="container fixed-bottom foot">
    <footer class="d-flex flex-wrap justify-content-between align-items-center py-1 my-1 border-top">
      <p class="col-md-4 mb-0 text-muted">© 2024 Project, Inc</p>

      <a href="/" class="col-md-4 d-flex align-items-center justify-content-center mb-3 mb-md-0 me-md-auto link-dark text-decoration-none">
        <svg class="bi me-2" width="40" height="32"><use xlink:href="#bootstrap"></use></svg>
      </a>

      <ul class="nav col-md-4 justify-content-end">
        <li class="nav-item"><router-link to="/" class="nav-link px-2 text-muted">Home</router-link></li>
        <li class="nav-item"><a href="#" class="nav-link px-2 text-muted">Features</a></li>
        <li class="nav-item"><a href="#" class="nav-link px-2 text-muted">Pricing</a></li>
        <li class="nav-item"><a href="#" class="nav-link px-2 text-muted">FAQs</a></li>
        <li class="nav-item"><a href="#" class="nav-link px-2 text-muted">About</a></li>
      </ul>
    </footer>
  </div>
</template>

<script>
export default {

}
</script>

<style scoped>

</style>