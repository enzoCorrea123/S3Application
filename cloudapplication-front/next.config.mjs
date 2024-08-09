/** @type {import('next').NextConfig} */
const nextConfig = {
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
