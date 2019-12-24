#!/usr/bin/env python

import rospy
from geometry_msgs.msg import Twist
from std_msgs.msg import String


class Communication:
    def __init__(self, robot):
        """
        Communication class constructor
        @param robot:
        """
        self.robot = robot
        self.cmd_pub = rospy.Publisher('cmd_vel', Twist, queue_size=1)
        self.sound_pub = rospy.Publisher('give_candy', String, queue_size=1)
        self.play_sound("onstart")

    def move(self, linear, angular):
        """
        Publishes move message
        @param linear:
        @param angular:
        """
        twist = Twist()
        twist.linear.x = -linear[0]
        twist.linear.y = -linear[1]
        twist.linear.z = -linear[2]
        twist.angular.x = angular[0]
        twist.angular.y = angular[1]
        twist.angular.z = angular[2]
        self.cmd_pub.publish(twist)
        self.robot.rate.sleep()

    def stop(self):
        """
        Publishes stop message
        """
        self.move([0, 0, 0], [0, 0, 0])

    def play_sound(self, string_msg):
        """
        Publishes play sound message
        @param string_msg:
        """
        msg = String()
        msg.data = string_msg
        self.sound_pub.publish(msg)
        self.robot.rate.sleep()