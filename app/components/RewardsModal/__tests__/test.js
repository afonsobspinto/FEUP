import 'react-native'
import React from 'react'
import { configure, shallow } from 'enzyme'
import toJson from 'enzyme-to-json'
import Adapter from 'enzyme-adapter-react-16'

import { PastActivityIcons } from '../PastActivityIcons'
import { RewardsModal } from '../RewardsModal'
import { RewardModalExperienceBar } from '../RewardModalExperienceBar'
import { RewardModalStars } from '../RewardModalStars'
import { BonusModal } from '../BonusModal'
import { RewardSound } from '../RewardSound'
import { activities, activitiesWithStatus } from '../../../constants/mockTestData'

configure({ adapter: new Adapter() })

describe('RewardsModal components', () => {
  it('renders PastActivityIcons correctly', () => {
    const wrapper = shallow(<PastActivityIcons
      currentActivity={0}
      activities={activities} />)
    expect(toJson(wrapper)).toMatchSnapshot()
    wrapper.setProps({ activities: activitiesWithStatus })
    wrapper.unmount()
  })

  it('renders RewardModalExperienceBar correctly', () => {
    const wrapper = shallow(<RewardModalExperienceBar progress={0.5} />)
    expect(toJson(wrapper)).toMatchSnapshot()
    wrapper.unmount()
  })

  it('renders RewardsModal correctly', () => {
    const wrapper = shallow(<RewardsModal
      currentActivity={0}
      activities={activities}
      xp={430}
      level={4}
      nextPress={jest.fn()}
      backPress={jest.fn()}
      playSounds={false} />)
    expect(toJson(wrapper)).toMatchSnapshot()
    wrapper.setProps({ activities: activitiesWithStatus })
    wrapper.instance().increaseProgress()
    wrapper.unmount()
  })

  it('renders RewardModalStars correctly', () => {
    jest.useFakeTimers()
    const wrapper = shallow(<RewardModalStars
      currentActivity={0}
      activities={activities}
      increaseProgress={jest.fn()} />)
    expect(toJson(wrapper)).toMatchSnapshot()
    wrapper.unmount()
  })

  it('renders BonusModal correctly', () => {
    jest.useFakeTimers()
    const wrapper = shallow(<BonusModal
      xp={0}
      level={1}
      nextPress={jest.fn()}
      playSounds />)
    expect(toJson(wrapper)).toMatchSnapshot()
    wrapper.instance().increaseProgress()
    wrapper.unmount()
  })

  it('renders RewardSound correctly 1', () => {
    jest.useFakeTimers()
    const wrapper = shallow(<RewardSound
      success={false}
      playSounds={false} />)
    expect(toJson(wrapper)).toMatchSnapshot()
    wrapper.unmount()
  })

  it('renders RewardSound correctly 2', () => {
    jest.useFakeTimers()
    const wrapper = shallow(<RewardSound
      success
      playSounds />)
    expect(toJson(wrapper)).toMatchSnapshot()
    wrapper.instance().playSound()
    wrapper.unmount()
  })
})
