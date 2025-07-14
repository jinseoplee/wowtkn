<script setup>
import PriceInfo from "./components/PriceInfo.vue";
</script>

<template>
  <div id="app-wrapper">
    <header class="app-header">
      <div class="container">
        <h1>
          <a href="/">Wowtkn</a>
        </h1>
      </div>
    </header>

    <main class="app-main">
      <div class="container">
        <PriceInfo />
      </div>
    </main>

    <footer class="app-footer"></footer>
  </div>
</template>

<style scoped></style>
