/** @type {import('tailwindcss').Config} */
module.exports = {
  content: ["./src/**/*.{html,ts}"],
  theme: {
    extend: {
      colors: {
        avrblue: "#005CB9",
      },
    },
  },
  plugins: [require("@tailwindcss/forms")],
};
