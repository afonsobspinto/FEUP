import React from 'react'
import { StyleSheet } from 'react-native'
import { Container, Header, Content, Footer, FooterTab, Button, Icon, Title, Body, Text } from 'native-base'
import { RoutinesScreen } from './RoutinesScreen'
import SettingsScreen from './SettingsScreen'
import ActionButton from 'react-native-action-button'
import PropTypes from 'prop-types'

export default class ParentMainMenuScreen extends React.Component {
  constructor (props) {
    super(props)

    this.state = {
      selectedTab: 'activity',
      title: 'Actividade'
    }
  }

  renderSelectedTab = () => {
    switch (this.state.selectedTab) {
      case 'activity':
        return (<Content />)
      case 'routines':
        return (<RoutinesScreen navigation={this.props.navigation} />)
      case 'settings':
        return (<SettingsScreen />)
    }
  }

  render () {
    return (
      <Container>
        <Header>
          <Body>
            <Title>{this.state.title}</Title>
          </Body>
        </Header>
        {this.renderSelectedTab()}
        <Footer>
          <FooterTab>
            <Button active={this.state.selectedTab === 'activity'}
              onPress={() => this.setState({ selectedTab: 'activity', title: 'Actividade' })} >
              <Icon name='md-filing' />
              <Text>Actividade</Text>
            </Button>
            <Button active={this.state.selectedTab === 'routines'}
              onPress={() => this.setState({ selectedTab: 'routines', title: 'Gerir Rotinas' })} >
              <Icon name='md-apps' />
              <Text>Gerir Rotinas</Text>
            </Button>
            <Button active={this.state.selectedTab === 'settings'}
              onPress={() => this.setState({ selectedTab: 'settings', title: 'Definições' })}>
              <Icon name='md-settings' />
              <Text>Definições</Text>
            </Button>
          </FooterTab>
        </Footer>
        {this.state.selectedTab === 'routines' && <ActionButton style={styles.actionButton} buttonColor='rgba(231,76,60,1)'>
          <ActionButton.Item buttonColor='#9b59b6' title='Nova Atividade' onPress={() => this.props.navigation.navigate('ActivityFormScreen')}>
            <Icon name='md-albuns' style={styles.actionButtonIcon} />
          </ActionButton.Item>
          <ActionButton.Item buttonColor='#1abc9c' title='Nova Rotina' onPress={() => this.props.navigation.navigate('RoutineFormScreen')}>
            <Icon name='md-alarm' style={styles.actionButtonIcon} />
          </ActionButton.Item>
        </ActionButton>}
      </Container>
    )
  }
}

ParentMainMenuScreen.propTypes = {
  navigation: PropTypes.object.isRequired
}

const styles = StyleSheet.create({
  actionButtonIcon: {
    fontSize: 20,
    height: 22,
    color: 'white'
  },
  actionButton: {
    marginBottom: 55
  }
})
