/** @type {import('next').NextConfig} */
const nextConfig = {
  output: "standalone",
    images: {
        remotePatterns: [
          {
            protocol: 'https',
            hostname: 'bucket-mi-73.s3.amazonaws.com',
          },
        ],
      },
};

export default nextConfig;
