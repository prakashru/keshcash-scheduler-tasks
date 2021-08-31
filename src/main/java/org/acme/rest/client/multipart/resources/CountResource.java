package org.acme.rest.client.multipart.resources;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.acme.rest.client.multipart.CounterBean;

import io.minio.BucketExistsArgs;
import io.minio.MakeBucketArgs;
import io.minio.MinioClient;

@Path("/minio")
public class CountResource {

	@Inject
	CounterBean counter;
	
	
	

	@GET
	@Path("/counter")
	@Produces(MediaType.TEXT_PLAIN)
	public String hello() {
		return "count: " + counter.get();
	}

	@GET
	@Path("/newbucket/{bucket}")
	public String createBucket(@PathParam(value = "bucket") String bucket) {
		boolean isCreated = false;
		MinioClient minioClient;
		try {
			
			minioClient = MinioClient.builder().endpoint("103.108.220.161", 7000, false)
					.credentials("minio", "minio123").build();
			
			try {
				if (!minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucket).build())) {
					minioClient.makeBucket(MakeBucketArgs.builder().bucket(bucket).build());
					System.out.println(bucket + " created successfully");
				}
				isCreated = true;
			} catch (Exception e) {
				e.printStackTrace();
			}

		} catch (Exception ex) {
			ex.printStackTrace();
		}

		if (isCreated)
			return "Created";
		else
			return "Failed";
	}

}
