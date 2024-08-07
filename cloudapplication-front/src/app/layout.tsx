import type { Metadata } from "next";
import { Inter } from "next/font/google";
import "./globals.css";
import { TaskContext } from "@/utils/Context/TaskContext";

const inter = Inter({ subsets: ["latin"] });

export const metadata: Metadata = {
  title: "Cloud-Frontend",
  description: "Front de upload de arquivos para o S3",
};

export default function RootLayout({
  children,
}: Readonly<{
  children: React.ReactNode;
}>) {
  return (
    <html lang="en">
      <TaskContext>
        <body className={inter.className}>{children}</body>
      </TaskContext>
    </html>
  );
}
