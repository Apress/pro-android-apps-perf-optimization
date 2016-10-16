#include <android_native_app_glue.h>
#include <android/sensor.h>
#include <android/log.h>

#define TAG "myapp"

typedef struct {
  // accelerometer
  const ASensor*     accelerometer_sensor;
  ASensorEventQueue* accelerometer_event_queue;
  // gyroscope
  const ASensor*     gyroscope_sensor;
  ASensorEventQueue* gyroscope_event_queue;
} my_user_data_t;

static int32_t on_key_event (struct android_app* app, AInputEvent* event)
{
  // use AKeyEvent_xxx APIs
  
  return 0; // or 1 if you have handled the event
}

static int32_t on_motion_event (struct android_app* app, AInputEvent* event)
{
  // use AMotionEvent_xxx APIs
  
  return 0; // or 1 if you have handled the event
}

static int32_t on_input_event (struct android_app* app, AInputEvent* event)
{
  int32_t type = AInputEvent_getType(event);
  int32_t handled = 0;
  
  switch (type) {
  case AINPUT_EVENT_TYPE_KEY:
    handled = on_key_event(app, event);
    break;
    
  case AINPUT_EVENT_TYPE_MOTION:
    handled = on_motion_event(app, event);
    break;
  }
  
  return handled;
}

static void on_input_changed (struct android_app* app) {}
static void on_init_window (struct android_app* app) {}
static void on_term_window (struct android_app* app) {}
static void on_window_resized (struct android_app* app) {}
static void on_window_redraw_needed (struct android_app* app) {}
static void on_content_rect_changed (struct android_app* app) {}

static void on_gained_focus (struct android_app* app)
{
  my_user_data_t* user_data = app->userData;
  if (user_data->accelerometer_sensor != NULL) {
    ASensorEventQueue_enableSensor(user_data->accelerometer_event_queue,
				   user_data->accelerometer_sensor);
    ASensorEventQueue_setEventRate(user_data->accelerometer_event_queue,
				   user_data->accelerometer_sensor, 1000000L/60);
  }
  if (user_data->gyroscope_sensor != NULL) {
    ASensorEventQueue_enableSensor(user_data->gyroscope_event_queue,
				   user_data->gyroscope_sensor);
    ASensorEventQueue_setEventRate(user_data->gyroscope_event_queue,
				   user_data->gyroscope_sensor, 1000000L/60);
  }
}

static void on_lost_focus (struct android_app* app)
{
  my_user_data_t* user_data = app->userData;
  if (user_data->accelerometer_sensor != NULL) {
    ASensorEventQueue_disableSensor(user_data->accelerometer_event_queue,
				    user_data->accelerometer_sensor);
  }
  if (user_data->gyroscope_sensor != NULL) {
    ASensorEventQueue_disableSensor(user_data->gyroscope_event_queue,
				    user_data->gyroscope_sensor);
  }
}

static void on_config_changed (struct android_app* app) {}
static void on_low_memory (struct android_app* app) {}
static void on_start (struct android_app* app) {}
static void on_resume (struct android_app* app) {}
static void on_save_state (struct android_app* app) {}
static void on_pause (struct android_app* app) {}
static void on_stop (struct android_app* app) {}
static void on_destroy (struct android_app* app) {}

static void on_app_command (struct android_app* app, int32_t cmd) {
  switch (cmd) {
  case APP_CMD_INPUT_CHANGED:
    on_input_changed(app);
    break;

  case APP_CMD_INIT_WINDOW:
    on_init_window(app);
    break;
    
  case APP_CMD_TERM_WINDOW:
    on_term_window(app);
    break;
    
  case APP_CMD_WINDOW_RESIZED:
    on_window_resized(app);
    break;

  case APP_CMD_WINDOW_REDRAW_NEEDED:
    on_window_redraw_needed(app);
    break;

  case APP_CMD_CONTENT_RECT_CHANGED:
    on_content_rect_changed(app);
    break;

  case APP_CMD_GAINED_FOCUS:
    on_gained_focus(app);
    break;

  case APP_CMD_LOST_FOCUS:
    on_lost_focus(app);
    break;

  case APP_CMD_CONFIG_CHANGED:
    on_config_changed(app);
    break;

  case APP_CMD_LOW_MEMORY:
    on_low_memory(app);
    break;

  case APP_CMD_START:
    on_start(app);
    break;

  case APP_CMD_RESUME:
    on_resume(app);
    break;

  case APP_CMD_SAVE_STATE:
    on_save_state(app);
    break;

  case APP_CMD_PAUSE:
    on_pause(app);
    break;

  case APP_CMD_STOP:
    on_stop(app);
    break;

  case APP_CMD_DESTROY:
    on_destroy(app);
    break;
  }
}

#define LOOPER_ID_USER_ACCELEROMETER  (LOOPER_ID_USER + 0)
#define LOOPER_ID_USER_GYROSCOPE      (LOOPER_ID_USER + 1)

#define NB_SENSOR_EVENTS 8

static int gyroscope_callback (int fd, int events, void* data)
{
  //__android_log_write(ANDROID_LOG_INFO, TAG, "gyroscope_callback");
  
  return 1;
}

static void list_all_sensors (ASensorManager* sm)
{
  ASensorList list;
  int i, n;
  n = ASensorManager_getSensorList(sm, & list);
  for (i = 0; i < n; i++) {
    const ASensor* sensor = list[i];
    const char* name = ASensor_getName(sensor);
    const char* vendor = ASensor_getVendor(sensor);
    int type = ASensor_getType(sensor);
    int min_delay = ASensor_getMinDelay(sensor);
    float resolution = ASensor_getResolution(sensor);
    
    __android_log_print(ANDROID_LOG_INFO, TAG, "%s (%s) %d %d %f",
			name, vendor, type, min_delay, resolution);
  }
}

void android_main (struct android_app* state)
{
  my_user_data_t user_data;
  ASensorManager* sm = ASensorManager_getInstance();
  
  app_dummy(); // don't forget that call
  
  list_all_sensors(sm);
  
  state->userData = & user_data;
  state->onAppCmd = on_app_command;
  state->onInputEvent = on_input_event;
  
  // accelerometer
  user_data.accelerometer_sensor =
    ASensorManager_getDefaultSensor(sm, ASENSOR_TYPE_ACCELEROMETER);
  user_data.accelerometer_event_queue =
    ASensorManager_createEventQueue(sm, state->looper,
				    LOOPER_ID_USER_ACCELEROMETER,
				    NULL, NULL);
  
  // gyroscope (callback-based)
  user_data.gyroscope_sensor =
    ASensorManager_getDefaultSensor(sm, ASENSOR_TYPE_GYROSCOPE);
  user_data.gyroscope_event_queue =
    ASensorManager_createEventQueue(sm, state->looper,
				    LOOPER_ID_USER_GYROSCOPE,
				    gyroscope_callback, NULL);
  
  while (1) {
    int ident;
    int events;
    struct android_poll_source* source;
    
    while ((ident = ALooper_pollAll(-1, NULL, &events, (void**)&source)) >= 0) {
      
      if ((ident == LOOPER_ID_MAIN) || (ident == LOOPER_ID_INPUT)) {
	// source should not be NULL but we check anyway
	if (source != NULL) {
	  // this will call on_app_command or on_input_event
	  source->process(source->app, source);
	}
      }
      
      // accelerometer events
      if (ident == LOOPER_ID_USER_ACCELEROMETER) {
	ASensorEvent sensor_events[NB_SENSOR_EVENTS];
	int i, n;
	while ((n = ASensorEventQueue_getEvents(user_data.accelerometer_event_queue,
						sensor_events,
						NB_SENSOR_EVENTS)) > 0) {
	  for (i = 0; i < n; i++) {
	    ASensorVector* vector = & sensor_events[i].vector;
	    __android_log_print(ANDROID_LOG_INFO, TAG,
				"%d accelerometer x=%f y=%f z=%f",
				i, vector->x, vector->y, vector->z);
	  }
	}
      }
      
      // other events
      
      if (state->destroyRequested != 0) {
	ASensorManager_destroyEventQueue(sm, user_data.accelerometer_event_queue);
	ASensorManager_destroyEventQueue(sm, user_data.gyroscope_event_queue);
	return;
      }
    }
    
    // do your rendering here when all the events have been processed
  }
}
