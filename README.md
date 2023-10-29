# ProgrammingAssignment1
Image classification and text detection application using AWS platform

## Services Used
* AWS S3
* AWS EC2 Instance
* AWS SQS Messaging Service 
* AWS Rekognition

## Steps Performed
- [x] AWS Learner's Lab Setup
- [x] AWS CLI configuration
- [x] AWS EC2 instance configuration
- [x] SSH access to EC2 instances
- [x] AWS SQS configuration
- [x] Image recognition Java application
- [x] Text detection java program
- [x] Java application deployment on EC2 instances

### Java application deployment on EC2 instances
* SSH to the EC2 instances using respective IP addresses.
* Provide AWS configuration using ```aws configure``` command.
* Install openjdk-java-19.0.1 using the command ```wget https://download.oracle.com/java/19/archive/jdk-19.0.1_linux-x64_bin.tar.gz```
* Extract the files using the command ```tar zxvf jdk-19.0.1_linux-x64_bin.tar.gz``` 
* Move the file using ```sudo mv jdk-19.0.1 /usr/share```
* Edit the etc/profile file using ```sudo vim /etc/profile``` in insert mode. Save the file with :wq after changes.
* Add the below details in the file.
```
    export JAVA_HOME=/usr/share/jdk-19.0.1
    export PATH=$JAVA_HOME/bin:$PATH
    export CLASSPATH=.:$JAVA_HOME/lib/dt.jar:$JAVA_HOME/lib/tools.jar
```
* Check the Java version and verify.
* Move the .jar file in EC2 instance using cyberduck.
* Run the command ```java -jar car-recognition-app-0.0.1-SNAPSHOT.jar``` for car recognition app, and  ```java -jar text-detection-app-0.0.1-SNAPSHOT.jar``` for text recognition app
 command to run text detection app. Both app can run in parallel.

## Step-by-step guide
### AWS Learner's Lab Setup 
* Login to AWS Learner's Lab through student account. The screen displays Readme file to display steps for the setup. AWS details shows the AWS credentials for the account.
* The AWS account can be accessed by clicking the red circle. It turns to green when the lab session is active. The toolbar also displays the remaining student credits.

### AWS CLI Setup
* After installing AWS CLI, then credentials and config can be setup using <br />
```aws configure``` <br />
* This asks for the AccessKey, SecertKey and SessionToken. This creates credentials amd config file in ```.aws/credentials``` directory. There can be multiple profiles to use different accounts.


### EC2 Instance Configuration
* Navigate to ```Services -> EC2```
* Navigate to ```Instances -> Launch Instances```
* Name the instance, select ```Amazon Linux```
* Select the ```t2.micro``` as instance type.
* Select or Create a new keypair.
* Select launch instance.

### SSH access to the EC2 instances
* As per requirement, the two instances are created.
* Download the ssh key (CS643-Cloud.pem)
* Navigate to the CS643-Cloud.pem file's location and run the below command. <br />
```ssh -i "CS643-Cloud.pem" ec2-user@ec2-50-17-36-229.compute-1.amazonaws.com``` here IPaddress is IPv4 address of EC2 instance.
* Type yes and the EC2 will be accessible from command line.

### AWS SQS configuration
* Navigate to ```Amazon SQS``` page.
* Select ```Create Queue```.
* Select queue type and provide the queue name. In our case, ```carsinformation.fifo```
* Put in the configuration details for SQS.
* Select encryption and access policy. It can be assigned to the LabRole also.
* Alternatively you can directly define the queue name in Code and AWS will automatically create a new SQS with the name if not available

### JAVA App for image recognition
* Fetch all the images from S3 bucket.
* Find the image labels and confidence score using AWS Rekognition service.
* The images with Car label and confidence score greater than 90% are marked, and their name is pushed to the AWS
 SQS message queue.
* In the end, -1 message is sent as a last message.
* Prepare the JAR file for deployment.

### JAVA App for text detection
* Fetch the messages one by one from AWS SQS queue.
* Run the text detection service using AWS Rekognition and find the detected text.
* If the queue is empty, it waits for the new messages.
* The images with detected text are written in ```ImageText.txt``` file with their respective indexes.
* Prepare the JAR file for deployment.

