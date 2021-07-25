<template>
  <div class="card-padding">
    <v-card width="300">
      <v-img
        class="white--text align-end"
        height="120px"
        src="https://cdn.vuetifyjs.com/images/cards/docks.jpg"
      >
        <v-card-title>{{ certification.name }}</v-card-title>
      </v-img>
      <div style="word-wrap: break-word; height: 70px;">
        <v-card-text
          v-if="certification.price == null || certification.price === 0 || parseInt(certification.price) === 0"
          class="align-end fill-height"
        >
          <strong>Free</strong>
          <v-rating
            half-increments
            v-model="certification.rating"
            small
            readonly
          ></v-rating>
        </v-card-text>
        <v-card-text v-else class="align-end fill-height">
          <strong
            >{{ certification.price }} {{ certification.currency }}</strong
          >
          <v-rating
            half-increments
            v-model="certification.rating"
            small
            readonly
          ></v-rating>
        </v-card-text>
      </div>
    </v-card>
  </div>
</template>

<script>
export default {
  props: {
    certification: {
      type: Object,
      required: true
    }
  }
};
</script>

<style lang="scss" scoped>
.card-padding {
  padding: 30px;
}
</style>
