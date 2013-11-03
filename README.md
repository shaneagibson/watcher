
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

        ...

        // ACT

        ...

        // ASSERT

        final List<RequestInvocations> issuedNotificationRequests = this.notificationServiceSpy.getWatchedInvocations();
        assertEquals(1, issuedNotificationRequests.size());
        assertEquals("/notification/sms", issuedNotificationRequests.getRequestUri());
        assertEquals(2, issuedNotificationRequests.getParameters());
        assertEquals("phoneNumber", issuedNotificationRequests.getParameters().get(0).getKey());
        assertEquals("07920053773", issuedNotificationRequests.getParameters().get(0).getValue());
        assertEquals("template", issuedNotificationRequests.getParameters().get(1).getKey());
        assertEquals("ACCOUNT_ACTIVATION_MESSAGE", issuedNotificationRequests.getParameters().get(1).getValue());

        final Matcher contentMatcher = Pattern.compile("\\{ \"token\" : \"([a-zA-Z0-9]{5})\" }").matcher(issuedNotificationRequests.getContent());

        assertTrue(contentMatcher.matches());

        final String token = contentMatcher.group(1);

        ...

    }
