import 'react-native'
import React from 'react'
import { configure, shallow } from 'enzyme'
import toJson from 'enzyme-to-json'
import Adapter from 'enzyme-adapter-react-16'

import { ChildExperienceBar } from '../ChildExperienceBar'
import { ShopItem } from '../ShopItem'
import { ShopTitle } from '../ShopTitle'
import { LevelUpModal } from '../LevelUpModal'
import { Avatar } from '../Avatar'
import { avatarItems } from '../../../assets/images/images'

configure({ adapter: new Adapter() })

describe('MainMenu components', () => {
  it('renders ChildExperienceBar correctly', () => {
    const wrapper = shallow(<ChildExperienceBar
      progress={0.5}
      level={2}
      onPress={jest.fn()}
      reward={{ name: 'test', photo: undefined }} />)
    expect(toJson(wrapper)).toMatchSnapshot()
    wrapper.unmount()
  })

  it('renders ShopItem correctly', () => {
    const wrapper = shallow(<ShopItem
      cost={100}
      disabled={false}
      purchased={false}
      purchaseItem={jest.fn()}
      id={0}
      equiped={false}
      toggleItem={jest.fn()}
      image={avatarItems[0].thumbnail} />)
    expect(toJson(wrapper)).toMatchSnapshot()
    wrapper.setProps({ disabled: true })
    wrapper.setProps({ purchased: true })
    wrapper.instance().purchaseItem()
    wrapper.instance().toggleItem()
    wrapper.unmount()
  })

  it('renders ShopTitle correctly', () => {
    const wrapper = shallow(<ShopTitle
      title={'test'} />)
    expect(toJson(wrapper)).toMatchSnapshot()
    wrapper.unmount()
  })

  it('renders LevelUpModal correctly', () => {
    const wrapper = shallow(<LevelUpModal
      show={false}
      level={3}
      xp={350}
      isReward={false}
      onClosed={jest.fn()}
      playSounds={false}
      reward={{ name: 'test', photo: undefined }} />)
    expect(toJson(wrapper)).toMatchSnapshot()
    wrapper.setProps({ isReward: true })
    wrapper.instance().onPress()
    wrapper.unmount()
  })

  it('renders Avatar correctly', () => {
    const wrapper = shallow(<Avatar
      equiped={[0]}
      gender={'M'}
      button={false} />)
    expect(toJson(wrapper)).toMatchSnapshot()
    wrapper.setProps({ gender: 'F', button: true })
    wrapper.unmount()
  })
})
