
watcher
=======

Watcher is a test utility for SOA projects, which allows the spying of RESTful resources though a simple java API.

Watcher provides a recording proxy for web requests:

    private final Watcher notificationServiceSpy = new Watcher(9000, "localhost", 9001);

    @Before
    public void setUp() {
        this.notificationServiceSpy.start();
    }

    @After
    public void tearDown() {
        this.notificationServiceSpy.stop();
    }

    @Test
    public void test() {

        // ARRANGE

        final RegExBodyRequestCaptor tokenCaptor = new RegExBodyRequestCaptor("\\{ \"token\" : \"([a-zA-Z0-9]{5})\" }");
        when(notificationServiceSpy.post("/notification/sms", parameters("phoneNumber", "07920053773"))).thenCapture(tokenCaptor);

        ...

        // ACT

        ...

        // ASSERT

        final String token = tokenCaptor.getCapturedValue();

        ...

    }
