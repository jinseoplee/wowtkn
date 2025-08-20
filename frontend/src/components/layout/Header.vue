<template>
  <header class="app-header">
    <div class="app-header__inner">
      <RouterLink to="/" class="app-header__logo">Wowtkn</RouterLink>
    </div>
  </header>
</template>

<style scoped>
.app-header {
  height: 80px;
}

.app-header__inner {
  height: 100%;
  display: flex;
  align-items: center;
  justify-content: center;
}

.app-header__logo {
  font-size: var(--font-size-4xl);
  font-weight: 800;
}

@media (min-width: 768px) {
  .app-header {
    height: 100px;
  }

  .app-header__logo {
    font-size: var(--font-size-5xl);
  }
}
</style>
