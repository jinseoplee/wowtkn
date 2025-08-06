<template>
  <header class="app-header">
    <div class="app-header__inner">
      <RouterLink to="/" class="app-header__logo">Wowtkn</RouterLink>
    </div>
  </header>
</template>

<style scoped>
.app-header {
  height: 60px;
}

.app-header__inner {
  height: 100%;
  display: flex;
  align-items: center;
}

.app-header__logo {
  font-size: var(--font-size-2xl);
  font-weight: bold;
}

@media (min-width: 768px) {
  .app-header__logo {
    font-size: var(--font-size-3xl);
  }
}
</style>
