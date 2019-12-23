import React from 'react'
import { configure, shallow } from 'enzyme'
import toJson from 'enzyme-to-json'
import Adapter from 'enzyme-adapter-react-16'
import { TouchableOpacity } from 'react-native'
import '../../../__mock__/xhr-mock'

import { ChildMainMenuScreen } from '../../../screens/child/ChildMainMenuScreen'

configure({ adapter: new Adapter() })

describe('ChildMainMenuScreen snapshot', () => {
  it('renders ChildMainMenuScreen correctly', () => {
    const wrapper = shallow(<ChildMainMenuScreen
      navigation={{ navigate: jest.fn() }}
      xp={150}
      level={1}
      showLevelUpModal={false}
      toggleLevelUpModal={jest.fn()}
      playSounds={false}
      gender={'M'}
      itemsEquiped={[0, 3]}
      childID={0}
      addRoutines={jest.fn()} />)
    expect(toJson(wrapper)).toMatchSnapshot()
    wrapper.find(TouchableOpacity).at(0).props().onPress()
    wrapper.find(TouchableOpacity).at(1).props().onPress()
    wrapper.instance().openShop()
    wrapper.instance().closeShop()
    wrapper.instance().onCloseModal()
    wrapper.instance().showRewardModal()
    wrapper.setProps({ showLevelUpModal: true })
    wrapper.instance().onCloseModal()
    wrapper.unmount()
  })
})