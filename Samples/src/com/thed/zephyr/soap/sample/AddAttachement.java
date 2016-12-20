/* D SOFTWARE INCORPORATED
 * Copyright 2007-2011 D Software Incorporated
 * All Rights Reserved.
 *
 * NOTICE: D Software permits you to use, modify, and distribute this 
file
 * in accordance with the terms of the license agreement accompanying 
it.
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an “AS IS” BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or 
implied.
 */
/*
* This is a sample of what can be done by using API?s with Zephyr through 
the JAVA coding language.
* By creating the .java files from the Zephyr WSDL, you can import them 
into your workspace and then
* call them in your custom program. 
* 
* Eclipse IDE for Java Developers- Version: Helios Service Release 2. 
Build id: 20110218-0911
* Java- Java JDK 1.6.0_25
* 
* Author: Swapna Kumar Vemula, Senior Product Support Engineer, D Software Inc.
*/
//Change the package to the package in your workspace

package com.thed.zephyr.soap.sample;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.xml.namespace.QName;

import com.thed.service.soap.wsdl.RemoteAttachment;
import com.thed.service.soap.wsdl.RemoteCriteria;
import com.thed.service.soap.wsdl.RemoteProject;
import com.thed.service.soap.wsdl.ZephyrServiceException;
import com.thed.service.soap.wsdl.ZephyrSoapService;
import com.thed.service.soap.wsdl.ZephyrSoapService_Service;

public class AddAttachement {
public static void main(String[] args) throws ZephyrServiceException 
{
 //String URL variable that holds the location of your Zephyr WSDL file
//This example has http://localhost but yours may be different 
String strURL = "http://localhost:85/flex/services/soap/zephyrsoapservice-v1?wsdl";
//Name of user performing operation. Test.manager is a default user name in a standard Zephyr installation
String username = "test.manager";
//Password for named user performing the operation. Test.manager is a default password for the test.manager user name
String password = "test.manager";
//Variable for token created in login process. Initialized to null value
String token = null;
//Creates project instance from RemoteProject object
RemoteProject project;
//Variable for searchCriteria collection list and initialization
java.util.List<RemoteCriteria> sC = null;
//Variable for RemoteProject collection list and initialization
java.util.List<RemoteProject> RP = null;
// This specifies the URL directly. The code will try to do the operations and throw an exception if not
try {
 //Initializes the URL data type with strURL created above
URL url = new URL(strURL);
//Create an instance of ZephyrSoapService. And initialize it with namespaceUri and LocalPart
ZephyrSoapService_Service serviceWithUrl = new ZephyrSoapService_Service(url, new QName("http://soap.service.thed.com/","ZephyrSoapService"));
//servicePortWithUrl is used for API calls to retrieve and add information in Zephyr
ZephyrSoapService servicePortWithUrl = serviceWithUrl.getZephyrSoapServiceImplPort();
//Login to Zephyr and pass the returned token into the token variable for later use
//Simple IF/ELSE statement to confirm.
token = servicePortWithUrl.login(username, password);
if(token == null)
System.out.println("Login Failed!");
else
System.out.println("Successfully Logged In. Your Token is:"  + token);
//Uses the RP variable to save the list of project IDs. Since sC is null and the return flag is true, it returns all projectIDs
//Token created at login passed for process authentication 
RP = servicePortWithUrl.getProjectsByCriteria(sC , true, token);
/*
* IF statement looks at the RP list collection, if it sees 
nothing in the list it will skip the loop
* Loop that uses the RemoteProject RP list to go through 
all the projects
* System.out.println for confirmation and example
*/
if(RP.isEmpty() == false)
{
for(int i = 0; i < RP.size(); i++)
{
project = RP.get(i);
System.out.println("\n" + "This name is: " + project.getName());
System.out.println("The project start date is: " + project.getStartDate());
}
}
else
System.out.println("\n No projects to display");
//System.out.println for confirmation

//Add Attachment to a testcase 

List<RemoteAttachment> remoteFiles = new ArrayList<RemoteAttachment>();
RemoteAttachment remoteFile = new RemoteAttachment();
//Initiate the parameters 
long entityId = 3; //Id of the entity used (here TestcaseId)
String entityType = "testcase";
remoteFile.setFileName("file.png");
System.out.println("Attachment File Name : " +remoteFile.getFileName());
remoteFile.setEntityId(entityId);
System.out.println("Attachment entity ID : " +remoteFile.getEntityId());
remoteFile.setEntityName(entityType);
System.out.println("Attachment entity Type : " +remoteFile.getEntityName());
remoteFile.setAttachmentURI("F:\\images\\file.png");//PATH to the attachment on your machine
String attachment = "123456";
remoteFile.setAttachment(attachment.getBytes());
remoteFiles.add(remoteFile);
try{
servicePortWithUrl.addAttachments(remoteFiles,token);
System.out.println("Attachment done*********************");
}catch (ZephyrServiceException e){ 
	e.printStackTrace();
}

//Logout 
servicePortWithUrl.logout(token);
System.out.println("\n" + "Logged Out");
} catch (MalformedURLException e) { 
e.printStackTrace(); //Prints exception(s)
}
}
}
