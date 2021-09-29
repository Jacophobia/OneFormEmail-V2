# OneFormEmail-V2
This is version two of the Email OneForm.

If you are just editing the OneForm-Email for the first time, please read the 
[Setup Guide](SETUP_GUIDE.md).

## [RequestCollector](src/main/java/oneForm/OneFormEmail_V2/RequestCollector.java)
    This class contains the endpoints which trigger the program to run. This is
    a listener program which is triggered when a request comes in. These 
    requests will come in from teamdynamix when the Email One Form Choices v2 is
    run. The requests contain the oneform ticket ID as a parameter. There are 
    three main endpoints. One for tickets that need to be escalated, one for 
    tickets that have been resolved, and one for spam. Each will be handled 
    differently by the rest of the program. 

## [RequestManager](src/main/java/oneForm/OneFormEmail_V2/RequestManager.java)
    This class will wait for a request to be made, and when one is, it will 
    launch a thread which runs the tasks contained in ProcessRequest.

## [ProcessRequest](src/main/java/oneForm/OneFormEmail_V2/ProcessRequest.java)
    This class will handle each individual request from the threadmanager. When
    a request is sent to the program, an object of this class will be created 
    which processes that request and organizes the flow of the program. This 
    class will call the other programs to create a department, andon, and count
    ticket using the information contained in a oneform ticket that it will 
    obtain. The program will start by creating a OneFormTicket object. It will 
    determine, based on the BSC office radio option selected in the OneForm, 
    which type of DepartmentTicket to create (Example: If the radio option 
    selected is Accounting, then an AccountingTicket object should be created, 
    if the Pathway radio option was selected then a PathwayTicket object should 
    be created.). When each ticket is created, it will add them all to an 
    ArrayList of GeneralTickets. At the end of the program, the program will 
    loop through the tickets in the ArrayList and call the uploadTicket method 
    in each. 

## [LoggingSupervisor](src/main/java/oneForm/OneFormEmail_V2/LoggingSupervisor.java)
    This is a class which simplifies the logging process. A single 
    LoggingSupervisor object will be created when the ProcessRequest object is 
    created. I recommend naming it something short and sweet like debug. This 
    will contain a history object which will be filled with loggingEvent 
    objects whenever the log method is called. Whenever you enter a new class 
    or method you should call the setClass or setMethod methods and put the new 
    class or the name of the new method as the parameter. This will help with 
    debugging later because the printed history will contain location specific 
    information when debug mode is turned on. 

## [ErrorTicket](src/main/java/oneForm/OneFormEmail_V2/ErrorTicket.java) extends Ticket
    This ticket is used to log errors since the papertrail only goes back a few
    days. Errors will be logged on these tickets and will appear on the report:
    Oneform Errors #19102. This will help us keep a record of all errors.

## [*GeneralTicket*](src/main/java/oneForm/OneFormEmail_V2/GeneralTicket.java) extends Ticket
    This is the basic model for all tickets that will be created in this 
    program. It contains some abstract methods which need to be overriden to 
    maintain functionality. 

### [AndonTicket](src/main/java/oneForm/OneFormEmail_V2/AndonTicket.java) extends GeneralTicket
    This ticket is created when the andon cord option is selected by the agent.

### [CountTicket](src/main/java/oneForm/OneFormEmail_V2/CountTicket.java) extends GeneralTicket
    Creates a count ticket for the count programs to keep track of. 

### [*DepartmentTicket*](src/main/java/oneForm/OneFormEmail_V2/DepartmentTicket.java) extends GeneralTicket
    An abstract generic department ticket containing all of the attributes and methods 
    used by the different department tickets. 

#### [PathwayTicket](src/main/java/oneForm/OneFormEmail_V2/DepartmentTickets/PathwayTicket.java) extends DepartmentTicket
    Creates a ticket for Pathway Worldwide and Pathway Connect contacts. 

#### [ByuiTicket](src/main/java/oneForm/OneFormEmail_V2/DepartmentTickets/ByuiTicket.java) extends DepartmentTicket
    Creates ticket for the SRR department.

#### [AccountingTicket](src/main/java/oneForm/OneFormEmail_V2/DepartmentTickets/AccountingTicket.java) extends DepartmentTicket
    Creates ticket for the SRR department.

#### [AdmissionsTicket](src/main/java/oneForm/OneFormEmail_V2/DepartmentTickets/AdmissionsTicket.java) extends DepartmentTicket
    Creates ticket for the SRR department.

#### [AdvisingTicket](src/main/java/oneForm/OneFormEmail_V2/DepartmentTickets/AdvisingTicket.java) extends DepartmentTicket
    Creates ticket for the SRR department.

#### [FinancialAidTicket](src/main/java/oneForm/OneFormEmail_V2/DepartmentTickets/FinancialAidTicket.java) extends DepartmentTicket
    Creates ticket for the SRR department.

#### [SrrTicket](src/main/java/oneForm/OneFormEmail_V2/DepartmentTickets/SrrTicket.java) extends DepartmentTicket
    Creates ticket for the SRR department.

## [Settings](src/main/java/oneForm/OneFormEmail_V2/Settings.java)
    The program is designed to be run with different settings depending on the 
    circumstances at the time you run the program. For example, if you are 
    testing, you can put the program into sandbox mode and you will not need to
    delete department tickets that the program makes since they will all be sent
    to the Teamdynamix Sandbox server. 
