# OneFormEmail-V2
This is version two of the Email Oneform.

## ProcessRequest
    Description:
    This class will handle each individual request from the threadmanager. When a request is sent to the program, an object of this class will be created which processes that request and organizes the flow of the program. This class will call the other programs to create a department, andon, and count ticket using the information contained in a oneform ticket that it will obtain. The program will start by creating a OneFormTicket object. It will determine, based on the BSC office radio option selected in the OneForm, which type of DepartmentTicket to create (Example: If the radio option selected is Accounting, then an AccountingTicket object should be created, if the Pathway radio option was selected then a PathwayTicket object should be created.). When each ticket is created, it will add them all to an ArrayList of GeneralTickets. At the end of the program, the program will loop through the tickets in the ArrayList and call the uploadTicket method in each. 
    
    Private:
    ArrayList<Ticket> tickets
    TeamDynamix api
    LoggingManager history

    Public:
    executeTask(): void
    createTickets(ticketID): void
    getDepartmentID(oneFormTicket): int

## LoggingManager
    Description:
    This is a class which simplifies the logging process. A single LoggingManager object will be created when the ProcessRequest object is created. I recommend naming it something short and sweet like debug. This will contain a history object which will be filled with loggingEvent objects whenever the log method is called. Whenever you enter a new class or method you should call the setClass or setMethod methods and put the new class or the name of the new method as the parameter. This will help with debugging later because the printed history will contain location specific information when debug mode is turned on. 
    
    Private:
    current_class: class
    current_method: String
    history: History

    Public:
    log(:String)
    log(:String, :Level)
    setMethod(:String)
    setClass(:class)

## *GeneralTicket*
    Description:
    This is the basic model for all tickets that will be created in this program. It contains some abstract methods which need to be overriden to maintain functionality. 
    
    extends Ticket
    Private:
    attributes: Map<attribute_id, value>
    
    Public:
    uploadTicket(): void

### AndonTicket
    Description:
    
    
    extends GeneralTicket
    Public:
    Private:

### CountTicket
    Description:
    
    
    extends GeneralTicket
    Public:
    Private:

### *DepartmentTicket*
    Description:
    
    
    extends GeneralTicket
    Public:
    Private:

#### PathwayTicket
    Description:
    
    
    extends DepartmentTicket
    Public:
    Private:

#### ByuiTicket
    Description:
    
    
    extends DepartmentTicket
    Public:
    Private:

#### AccountingTicket
    Description:
    
    
    extends DepartmentTicket
    Public:
    Private:

#### AdmissionsTicket
    Description:
    
    
    extends DepartmentTicket
    Public:
    Private:

#### AdvisingTicket
    Description:
    
    
    extends DepartmentTicket
    Public:
    Private:

#### FinancialAidTicket
    Description:
    
    
    extends DepartmentTicket
    Public:
    Private:

#### SrrTicket
    Description:
    
    
    extends DepartmentTicket
    Public:
    Private:
