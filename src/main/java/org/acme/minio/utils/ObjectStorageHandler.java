package org.acme.minio.utils;

import io.minio.MakeBucketArgs;
import io.minio.MinioClient;
import io.minio.Result;
import io.minio.UploadObjectArgs;
import io.minio.http.Method;
import io.minio.messages.Item;

import io.minio.ListObjectsArgs;

import java.util.ArrayList;
import java.util.List;



import io.minio.BucketExistsArgs;
import io.minio.GetPresignedObjectUrlArgs;

public class ObjectStorageHandler {

	private MinioClient minioClient;

	public ObjectStorageHandler() {
		
//		 MinioClient minioClient1 =
//		          MinioClient.builder()
//		              .endpoint("103.108.220.161", 7000, false)
//		              .credentials("minio", "minio123")
//		              .build();

		
		
		this.minioClient = MinioClient.builder()
				.endpoint("103.108.220.161", 7000, false)
				.credentials("minio", "minio123")
				.build();
	}

	public boolean createBucketIfNotExists(String bucketName) {
		try {
			if (!this.minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucketName).build())) {
				minioClient.makeBucket(MakeBucketArgs.builder().bucket(bucketName).build());
				System.out.println(bucketName + " created successfully");
			}
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	public boolean uploadObjectFromPath(String bucketName, String objectName, String path) {
		try {
			this.minioClient.uploadObject(
					UploadObjectArgs.builder().bucket(bucketName).object(objectName).filename(path).build());
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	public String getFileEmbedUrl(String bucketName, String objectName) {

		try {
			String url = minioClient.getPresignedObjectUrl(GetPresignedObjectUrlArgs.builder().method(Method.GET)
					.bucket(bucketName).object(objectName).expiry(60 * 60 * 24).build());
			return url;

		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	public List<String> getUserDocumentUrls(String bucketName, String userNamePrefix) {
		Iterable<Result<Item>> results = this.minioClient.listObjects(
				ListObjectsArgs.builder().bucket(bucketName).prefix(userNamePrefix).recursive(true).build());

		List<String> fileList = new ArrayList<>();

		for (Result<Item> result : results) {
			Item item = null;
			try {
				item = result.get();
				String fileUrl = this.getFileEmbedUrl(bucketName, item.objectName());
				fileList.add(fileUrl);
				System.out.println(":" + item.size() + "\t" + item.objectName());

			} catch (Exception e) {
				e.printStackTrace();
			}

		}

		return fileList;
	}
	
	public static void main(String args[]) {
		ObjectStorageHandler mc = new ObjectStorageHandler();
		
		String aUserName = "BUser";
		String userUniqueId="id02";
		
		String bucketNameFromUserName = aUserName.substring(0,1).toLowerCase()+"000";
		
		String objectName = userUniqueId+"/"+"WhatsAppImage.jpeg";
		String filePath = "/Users/prakashdutta/Desktop/WhatsAppImage.jpeg";
		
		mc.createBucketIfNotExists(bucketNameFromUserName);
		mc.uploadObjectFromPath(bucketNameFromUserName, objectName, filePath);
		
		try {
			Thread.sleep(100);
		} catch(InterruptedException ex) {
			ex.printStackTrace();
		}
		
		String fileUrl = mc.getFileEmbedUrl(bucketNameFromUserName, objectName);
		System.out.println("file Path "+ fileUrl);
	}

}
