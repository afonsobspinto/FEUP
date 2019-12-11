#!/usr/bin/env python

from robot import *
import rospy

from settings import log


def main():
    log("Candybot5")
    rospy.init_node('main')
    try:
        r = Robot(Position(2, 2), rows=5)
        r.start()
    except rospy.ROSInterruptException:
        pass


if __name__ == '__main__':
    main()
