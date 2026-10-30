# ComposeNews Redesign

UI/UX redesign mockups for ComposeNews, exported from the design canvas as a single static page.

- [`index.html`](index.html) — every screen, open it in a browser. Two sections:
  - **Android · Material 3 Expressive**: phone (markets, detail, favorites, loading/empty/error), foldable, tablet, desktop, Google TV, Wear OS.
  - **iOS · Liquid Glass**: iPhone (markets, detail, favorites, states), iPad, Apple Watch.
- [`tokens.json`](tokens.json) — the design tokens (color roles for light/dark, type, spacing, radii) the screens use. The color roles match `library:designsystem`'s `Color.kt`.

Notes:

- The export is static: menus, filters and favorite toggles show one state each. Links between screens (row → detail, bottom navigation) jump to the matching screen on the page.
- Coin logos are `[COIN LOGO]` placeholders and all prices are sample data.
- Fonts (Roboto Flex, Material Symbols Rounded) load from Google Fonts.
