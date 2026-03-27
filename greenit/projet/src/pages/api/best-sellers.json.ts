export const GET = async () => {
  const products = [
    {
      id: "1",
      title: "Sodastream Classics Pink Grap...",
      image: "https://cdn.shopify.com/s/files/1/2798/7364/files/PinkGrapefruitZero.png?&width=440",
      originalPrice: "$6.99",
      currentPrice: "$5.24",
    },
    {
      id: "2",
      title: "Sodastream MTN DEW Code Red Ch...",
      image: "https://cdn.shopify.com/s/files/1/2798/7364/files/MountainDewCodeRedZero.png?&width=440",
      originalPrice: "$7.99",
      currentPrice: "$5.99",
    },
    {
      id: "3",
      title: "Sodastream Fizz & Go Easy...",
      image: "https://cdn.shopify.com/s/files/1/2798/7364/files/BOTTLE-MRHI-US22_1.png?width=440",
      originalPrice: "$34.99",
      currentPrice: "$26.24",
    },
    {
      id: "4",
      title: "Sodastream Fizz & Go Easy...",
      image: "https://cdn.shopify.com/s/files/1/2798/7364/files/BOTTLE-MRHI-US24_1.png?width=440",
      originalPrice: "$34.99",
      currentPrice: "$26.24",
    },
    {
      id: "5",
      title: "Sodastream Pepsi Wild Cherry Z...",
      image: "https://cdn.shopify.com/s/files/1/2798/7364/files/Pepsi_Wild_Cherry_Zero.png?width=440",
      originalPrice: "$7.99",
      currentPrice: "$5.99",
    }
  ];

  return new Response(JSON.stringify(products), {
    status: 200,
    headers: {
      "Content-Type": "application/json"
    }
  });
};